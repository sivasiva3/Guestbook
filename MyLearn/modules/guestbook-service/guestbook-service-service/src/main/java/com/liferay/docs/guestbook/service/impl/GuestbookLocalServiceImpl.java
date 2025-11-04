/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.docs.guestbook.service.impl;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.docs.guestbook.exception.GuestbookEntryNameException;
import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.GuestbookEntryLocalService;
import com.liferay.docs.guestbook.service.base.GuestbookLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author liferay
 */
@Component(
	property = "model.class.name=com.liferay.docs.guestbook.model.Guestbook",
	service = AopService.class
)
public class GuestbookLocalServiceImpl extends GuestbookLocalServiceBaseImpl {
	@Indexable(type=IndexableType.REINDEX)
	public Guestbook addGuestBook(long userId,String name,ServiceContext service) throws PortalException {
		long groupId=service.getScopeGroupId();
		User user=userLocalService.getUserById(userId);
		validate(name);
		Date now = new Date();
		long guestbookId = counterLocalService.increment();
		Guestbook guestbook= guestbookPersistence.create(guestbookId);
		guestbook.setUuid(PortalUUIDUtil.generate());
		guestbook.setUserId(userId);
		guestbook.setCompanyId(user.getCompanyId());
		guestbook.setGroupId(groupId);
		guestbook.setUserName(user.getFirstName());
		guestbook.setCreateDate(service.getCreateDate(now));
		guestbook.setName(name);
		guestbook.setModifiedDate(service.getModifiedDate(now));
		guestbook.setExpandoBridgeAttributes(service);
		guestbookPersistence.update(guestbook);
		resourceLocalService.addResources(user.getCompanyId(),groupId,userId,Guestbook.class.getName(),guestbookId,false,true,true);
		AssetEntry assetEntry=assetEntryLocalService.updateEntry(guestbook.getUserId(),guestbook.getGroupId(),guestbook.getCreateDate(),guestbook.getModifiedDate(),Guestbook.class.getName(),
				guestbookId,guestbook.getUuid(),0,service.getAssetCategoryIds(),service.getAssetTagNames(),true,true,guestbook.getCreateDate(),
				null,null,null,ContentTypes.TEXT_HTML,guestbook.getName(),null,null,null,null,0,0,service.getAssetPriority());
		assetLinkLocalService.updateLinks(service.getUserId(), assetEntry.getEntryId(), service.getAssetLinkEntryIds(), AssetLinkConstants.TYPE_RELATED);
		return guestbook;	
	}
	@Indexable(type=IndexableType.REINDEX)
	public Guestbook updateGuestbook(long userId,long guestbookId,String name,ServiceContext service)throws PortalException,SystemException{
		Date now =new Date();
		validate(name);
		Guestbook guestbook=getGuestbook(guestbookId);
		User user=userLocalService.getUser(userId);
		guestbook.setUserId(userId);
		guestbook.setName(name);
		guestbook.setUserName(user.getFullName());
		guestbook.setModifiedDate(service.getModifiedDate(now));
		guestbook.setExpandoBridgeAttributes(service);
		guestbookPersistence.update(guestbook);
		resourceLocalService.updateResources(service.getCompanyId(),service.getScopeGroupId(),Guestbook.class.getName(),guestbookId,service.getModelPermissions());
		return guestbook;
		
	}
	@Indexable(type=IndexableType.DELETE)
	public Guestbook deleteGuestbook(long guestbookId,ServiceContext service) throws PortalException,SystemException{
		Guestbook guestbook=getGuestbook(guestbookId);
		resourceLocalService.deleteResource(service.getCompanyId(),Guestbook.class.getName(),ResourceConstants.SCOPE_INDIVIDUAL,guestbookId);
		AssetEntry assetEntry=assetEntryLocalService.fetchEntry(Guestbook.class.getName(), guestbookId);
		assetLinkLocalService.deleteLinks(assetEntry.getEntryId());
		assetEntryLocalService.deleteEntry(assetEntry);
		List<GuestbookEntry> entries= _guestbookEntryLocalService.getGuestbookEntries(
				service.getScopeGroupId(),guestbookId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		for(GuestbookEntry entry:entries) {
			_guestbookEntryLocalService.deleteGuestbookEntry(entry.getEntryId());
		}
		
		 guestbook = deleteGuestbook(guestbook);
		 return guestbook;
	}
	public List<Guestbook> getGuestbooks(long groupId){
		return guestbookPersistence.findByGroupId(groupId);
	}
	public List<Guestbook> getGuestbooks(long groupId, int start, int end, OrderByComparator<Guestbook> obc){
		return guestbookPersistence.findByGroupId(groupId,start,end,obc);
	}
	public List<Guestbook> getGuestbooks(long groupId, int start, int end){
		return guestbookPersistence.findByGroupId(groupId,start,end);
	}
	public int getGuestbookCount(long groupId){
		return guestbookPersistence.countByGroupId(groupId);
	}
	

	protected void validate(String name) throws PortalException {
		if(Validator.isNull(name)) {
			throw new GuestbookEntryNameException();
		}
	}
	@Reference
	 private GuestbookEntryLocalService _guestbookEntryLocalService;
}
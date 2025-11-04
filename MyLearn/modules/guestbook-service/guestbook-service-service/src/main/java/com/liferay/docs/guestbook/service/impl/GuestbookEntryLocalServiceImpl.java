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
import com.liferay.docs.guestbook.exception.GuestbookEntryEmailException;
import com.liferay.docs.guestbook.exception.GuestbookEntryMessageException;
import com.liferay.docs.guestbook.exception.GuestbookEntryNameException;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.base.GuestbookEntryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.UUID;

import org.osgi.service.component.annotations.Component;

/**
 * @author liferay
 */
@Component(
	property = "model.class.name=com.liferay.docs.guestbook.model.GuestbookEntry",
	service = AopService.class
)

public class GuestbookEntryLocalServiceImpl extends GuestbookEntryLocalServiceBaseImpl {
//	@Reference
//	 private GuestbookEntryLocalService _guestbookEntryLocalService;
	@Indexable(type=IndexableType.REINDEX)
	public GuestbookEntry addEntry(long userId,long guestbookId,String name,String email,String message,ServiceContext service) throws PortalException {
		long groupId = service.getScopeGroupId();
		User user=userLocalService.getUserById(userId);
		Date now=new Date();
		validate(name,email,message);
		long entryId=counterLocalService.increment();
		GuestbookEntry entry=guestbookEntryPersistence.create(entryId);
		entry.setUuid(UUID.randomUUID().toString());
		entry.setCreateDate(service.getCreateDate(now));
		entry.setModifiedDate(service.getModifiedDate(now));
		entry.setGuestbookId(guestbookId);
		entry.setName(name);
		entry.setEmail(email);
		entry.setMessage(message);
		entry.setUserId(userId);
		entry.setCompanyId(user.getCompanyId());
		entry.setUserName(user.getFullName());
		entry.setGroupId(groupId);
		entry.setExpandoBridgeAttributes(service);
		guestbookEntryPersistence.update(entry);
		resourceLocalService.addResources(user.getCompanyId(), groupId,userId,GuestbookEntry.class.getName(),entryId,false,true,true);
		
		AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId, groupId, entry.getCreateDate(), entry.getModifiedDate(), GuestbookEntry.class.getName(), 
										entryId, entry.getUuid(), 0, service.getAssetCategoryIds(), service.getAssetTagNames(), 
										true, true, null, null, null, null, ContentTypes.TEXT_HTML, entry.getMessage(), null, null, null, null, 0, 0, null);
		assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(), service.getAssetLinkEntryIds(), AssetLinkConstants.TYPE_RELATED);
		return entry;
		
	}	
	protected void validate(String name,String email,String message)throws PortalException {
		if(Validator.isNull(message)) {
			throw new GuestbookEntryMessageException();
			}
		if(Validator.isNull(name)) {
			throw new GuestbookEntryNameException();
		}
		if(Validator.isNull(email)) {
			throw new GuestbookEntryEmailException();
		}
	}
	@Indexable(type=IndexableType.REINDEX)
	public GuestbookEntry updateGuestbookEntry(long entryId,long userId,long guestbookId,String name,String email,String message,ServiceContext service) throws PortalException{
		Date now =new Date();
		validate(name,email,message);
		GuestbookEntry entry=guestbookEntryPersistence.findByPrimaryKey(entryId);
		User user=userLocalService.getUserById(userId);
		
		entry.setUserId(userId);
		entry.setUserName(user.getFullName());
		entry.setModifiedDate(service.getCreateDate(now));
		entry.setName(name);
		entry.setEmail(email);
		entry.setMessage(message);
		entry.setExpandoBridgeAttributes(service);
		guestbookEntryPersistence.updateImpl(entry);
		resourceLocalService.updateResources(user.getCompanyId(),service.getScopeGroupId(),GuestbookEntry.class.getName(),entryId,service.getModelPermissions());
		return entry;
		
	}
	@Indexable(type=IndexableType.DELETE)
	public GuestbookEntry deleteGuestbookEntry(GuestbookEntry entry){
		guestbookEntryPersistence.remove(entry);
		try {
			resourceLocalService.deleteResource(entry.getCompanyId(),GuestbookEntry.class.getName(),ResourceConstants.SCOPE_INDIVIDUAL, entry.getEntryId());
			AssetEntry assetEntry=assetEntryLocalService.fetchEntry(GuestbookEntry.class.getName(), entry.getEntryId());
			assetLinkLocalService.deleteLinks(assetEntry.getEntryId());
			assetEntryLocalService.deleteEntry(assetEntry);
			
		} catch (PortalException e) {
			e.printStackTrace();
		}
		return entry;	
	}
	public GuestbookEntry deleteGuestbookEntry(long entryId) throws PortalException{
		GuestbookEntry entry=guestbookEntryPersistence.findByPrimaryKey(entryId);
		return deleteGuestbookEntry(entry);	
	}
}
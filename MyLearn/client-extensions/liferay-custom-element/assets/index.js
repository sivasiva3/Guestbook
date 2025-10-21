import './style.css';
(function () {

	class VanillaCounter extends HTMLElement {
		constructor() {
			super();

			const shadow = this.attachShadow({ mode: 'open' });
            const message = document.createElement('div');
            message.textContent = 'Hello Liferay Users :)';
            shadow.appendChild(message);
		}
	}
	customElements.define('vanilla-counter', VanillaCounter);
})();
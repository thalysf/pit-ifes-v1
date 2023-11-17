import { Component, HostBinding } from '@angular/core';
import { loadMessages, locale } from 'devextreme/localization';
import { AuthService, ScreenService, AppInfoService } from './shared/services';
import ptMessages from 'devextreme/localization/messages/pt.json';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    @HostBinding('class') get getClass() {
        return Object.keys(this.screen.sizes).filter(cl => this.screen.sizes[cl]).join(' ');
    }

    constructor(
        private authService: AuthService,
        private screen: ScreenService,
        public appInfo: AppInfoService
    ) {
        loadMessages(ptMessages);

        locale(navigator.language);
    }

    isAuthenticated() {
        return this.authService.loggedIn;
    }
}

import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {SingleCardModule} from 'src/app/layouts';

@Component({
    selector: 'app-not-authorized-container',
    template: `
        <app-single-card [title]="title" [description]="description">
            <router-outlet></router-outlet>
        </app-single-card>
    `,
    styles: [`
        :host {
            width: 100%;
            height: 100%;
        }
    `]
})
export class NotAuthorizedContainerComponent {

    constructor(private router: Router) {
    }

    get title() {
        const path = this.router.url.split('/')[1];
        switch (path) {
            case 'login':
                return 'Login';
            case 'reset-password':
                return 'Resetar senha';
            case 'create-account':
                return 'Criar conta';
            case 'change-password':
                return 'Mudar senha';
            default:
                return '';
        }
    }

    get description() {
        const path = this.router.url.split('/')[1];
        switch (path) {
            case 'reset-password':
                return 'Por favor, insira o endereço de e-mail que usaram para te registrar no sistema e nós enviaremos um link para resetar sua senha, através deste endereço.';
            default:
                return '';
        }
    }
}

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        SingleCardModule,
    ],
    declarations: [NotAuthorizedContainerComponent],
    exports: [NotAuthorizedContainerComponent]
})
export class NotAuthorizedContainerModule {
}

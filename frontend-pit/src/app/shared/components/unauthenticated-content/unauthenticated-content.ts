import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {SingleCardModule} from 'src/app/layouts';

@Component({
    selector: 'app-unauthenticated-content',
    templateUrl: 'unauthenticated-content.html',
    styleUrls: [
        'unauthenticated-content.scss'
    ]
})
export class UnauthenticatedContentComponent {

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
                return 'Por favor, insira o endereço de e-mail que usaram para te registrar no sistema e nós enviaremos um código para resetar sua senha, através deste endereço.';
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
    declarations: [UnauthenticatedContentComponent],
    exports: [UnauthenticatedContentComponent]
})
export class UnauthenticatedContentModule {
}

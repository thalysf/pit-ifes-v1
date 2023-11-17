import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {DxFormModule} from 'devextreme-angular/ui/form';
import {DxLoadIndicatorModule} from 'devextreme-angular/ui/load-indicator';
import notify from 'devextreme/ui/notify';
import {AuthService} from '../../../shared/services';


@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: [
        './login-form.component.scss',
        '../../../shared/components/formulario-dinamico/formulario-dinamico.component.css'
    ]
})
export class LoginFormComponent {
    loading = false;
    formData: any = {};
    private _subscribeApi:any = null;

    linkedins = [
        {
            nome: 'João Vitor Natali',
            link: 'https://www.linkedin.com/in/jo%C3%A3o-vitor-natali-547b43219/'
        },
        {
            nome: 'Thalys Fabrete Cândido',
            link: 'https://www.linkedin.com/in/thalys-fabrete-c%C3%A2ndido-20443b160/'
        },
    ]

    constructor(private authService: AuthService, private router: Router) {
    }

    async onSubmit(e: Event) {
        e.preventDefault();
        const {email, password} = this.formData;
        this.loading = true;

        this._subscribeApi = await this.authService.logIn(email, password).subscribe(
            (response: any) => {
                localStorage.clear();

                localStorage.setItem('email', email);
                localStorage.setItem('jwtToken', response['jwt']['accessToken']);
                localStorage.setItem('tokenType', response['jwt']['tokenType']);
                localStorage.setItem('userId', response['usuario']['userId']);
                localStorage.setItem('roles', JSON.stringify(response['usuario']['roles']));

                this.loading = false;
                this.router.navigate(['/']);
            },
            (error: any) => {
                this.loading = false;
                notify(error, 'error', 2000);
            }
        );
    }

    onCreateAccountClick = () => {
        this.router.navigate(['/create-account']);
    }
}

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        DxFormModule,
        DxLoadIndicatorModule
    ],
    declarations: [LoginFormComponent],
    exports: [LoginFormComponent]
})
export class LoginFormModule {
}

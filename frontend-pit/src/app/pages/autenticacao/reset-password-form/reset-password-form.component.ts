import {CommonModule} from '@angular/common';
import {Component, NgModule, OnDestroy} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {DxFormModule} from 'devextreme-angular/ui/form';
import {DxLoadIndicatorModule} from 'devextreme-angular/ui/load-indicator';
import notify from 'devextreme/ui/notify';
import {AuthService} from '../../../shared/services';

const notificationText = 'Mandamos um código para alterar sua senha. Cheque sua caixa de entrada do e-mail.';

@Component({
    selector: 'app-reset-password-form',
    templateUrl: './reset-password-form.component.html',
    styleUrls: ['./reset-password-form.component.scss']
})
export class ResetPasswordFormComponent implements OnDestroy{
    loading = false;
    formData: any = {};
    subscription: any;

    constructor(private authService: AuthService, private router: Router) {
    }

    ngOnDestroy(): void {
        if(this.subscription) this.subscription.unsubscribe();
    }

    async onSubmit(e: Event) {
        e.preventDefault();
        const {email} = this.formData;
        this.loading = true;

        this.subscription = this.authService.sendRecoverEmail(email)
            .subscribe(response => {
                this.loading = false;
                notify(notificationText, 'success', 2500);
                localStorage.setItem('recoveryEmail', email);
                this.router.navigate(['/change-password']);
            }, error => {
                this.loading = false;
                notify(error.message, 'error', 2000);
            });
    }
}

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        DxFormModule,
        DxLoadIndicatorModule
    ],
    declarations: [ResetPasswordFormComponent],
    exports: [ResetPasswordFormComponent]
})
export class ResetPasswordFormModule {
}

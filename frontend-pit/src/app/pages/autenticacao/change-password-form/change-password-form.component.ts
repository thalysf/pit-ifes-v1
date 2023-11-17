import {CommonModule} from '@angular/common';
import {Component, NgModule, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {ValidationCallbackData} from 'devextreme/ui/validation_rules';
import {DxFormModule} from 'devextreme-angular/ui/form';
import {DxLoadIndicatorModule} from 'devextreme-angular/ui/load-indicator';
import notify from 'devextreme/ui/notify';
import {AuthService} from '../../../shared/services';


@Component({
    selector: 'app-change-passsword-form',
    templateUrl: './change-password-form.component.html'
})
export class ChangePasswordFormComponent implements OnInit {
    loading = false;
    formData: any = {};
    recoveryEmail?: string | null;

    constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.getRecoveryEmail();
    }

    private getRecoveryEmail(){
        this.recoveryEmail = localStorage.getItem('recoveryEmail');
        if(!this.recoveryEmail) this.redirectLogin();
    }

    async onSubmit(e: Event) {
        e.preventDefault();
        const {password, recoveryCode} = this.formData;
        this.loading = true;

        this.authService.changePassword(this.recoveryEmail!, password, recoveryCode)
            .subscribe(response => {
                this.loading = false;
                localStorage.clear();
                this.redirectLogin();
            }, error => {
                this.loading = false;
                notify(error.message, 'error', 2000);
            });
    }

    confirmPassword = (e: ValidationCallbackData) => {
        return e.value === this.formData.password;
    }

    private redirectLogin(){
        this.router.navigate(['/login']);
    }
}

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        DxFormModule,
        DxLoadIndicatorModule
    ],
    declarations: [ChangePasswordFormComponent],
    exports: [ChangePasswordFormComponent]
})
export class ChangePasswordFormModule {
}

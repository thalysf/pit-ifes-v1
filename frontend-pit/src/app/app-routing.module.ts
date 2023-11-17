import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
    ChangePasswordFormComponent,
    CreateAccountFormComponent,
    LoginFormComponent,
    ResetPasswordFormComponent
} from './shared/components';
import {AuthGuardService} from './shared/services';
import {DxButtonModule, DxFormModule} from 'devextreme-angular';
import {ParentResourceSchemaResolve, ResourceSchemaResolve} from './shared/services/resource-schema.resolver';
import {FormDetailComponent, FormDetailModule} from './shared/components/form-detail-rotas/form-detail.component';
import {SchemaService} from './shared/services/schema-service';
import {AppSchema} from './app.schema';
import {PerfilComponent} from './pages/perfil/perfil.component';
import {BaseListComponent, BaseListModule} from "./shared/components/table-list/base-list.component";
import {TableAssociacaoModule} from "./shared/components/table-list/table-associacao/table-associacao.component";
import {FormularioDinamicoModule} from "./shared/components/formulario-dinamico/formulario-dinamico.component";
import {ToolbarModule} from "./shared/components/toolbar/toolbar.component";

export const rotasDinamicas: Routes = [
    {
        path: ':entity',
        resolve: {schema: ResourceSchemaResolve}, children: [
            {
                path: '',
                component: BaseListComponent,
                canActivate: [AuthGuardService],
            },
            {
                path: 'novo',
                component: FormDetailComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: ':id', children: [
                    {
                        path: ':targetEntity/novo',
                        component: FormDetailComponent,
                        canActivate: [AuthGuardService],
                        resolve: {schema: ParentResourceSchemaResolve},
                    },
                    {
                        path: ':targetEntity/:targetId',
                        component: FormDetailComponent,
                        canActivate: [AuthGuardService],
                        resolve: {schema: ParentResourceSchemaResolve}
                    },
                    {
                        path: '',
                        component: FormDetailComponent,
                        canActivate: [AuthGuardService],
                        pathMatch: 'full',
                    },
                ]
            },
        ]
    }
];


const routes: Routes = [
    {
        path: 'login',
        component: LoginFormComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'perfil',
        component: PerfilComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'reset-password',
        component: ResetPasswordFormComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'create-account',
        component: CreateAccountFormComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'change-password',
        component: ChangePasswordFormComponent,
        canActivate: [AuthGuardService]
    },
    ...rotasDinamicas,
    {
        path: '**',
        redirectTo: 'perfil'
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes),
        DxFormModule,
        DxButtonModule,
        BaseListModule,
        TableAssociacaoModule,
        FormDetailModule,
        FormularioDinamicoModule,
        ToolbarModule
    ],
    providers: [
        AuthGuardService,
        ResourceSchemaResolve,
        {
            provide: SchemaService,
            useFactory: () => new AppSchema(),
        }
    ],
    exports: [RouterModule],
    declarations: [
        PerfilComponent
    ]
})
export class AppRoutingModule {
}

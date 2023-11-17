import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {SideNavOuterToolbarModule, SingleCardModule} from './layouts';
import {
    ChangePasswordFormModule,
    CreateAccountFormModule,
    FooterModule,
    LoginFormModule,
    ResetPasswordFormModule
} from './shared/components';
import {AppInfoService, AuthService, ScreenService} from './shared/services';
import {UnauthenticatedContentModule} from './shared/components/unauthenticated-content/unauthenticated-content';
import {AppRoutingModule} from './app-routing.module';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AddHeaderInterceptor} from "./shared/services/httpInterceptor";
import {ProjetoModule} from "./pages/projeto/projeto.module";
import {PitModule} from "./pages/pit/pit.module";
import {AtividadesModule} from "./pages/atividades/atividades.module";
import {PortariaModule} from "./pages/portaria/portaria.module";
import {CursosModule} from "./pages/cursos/cursos.module";

@NgModule({
    declarations: [
        AppComponent,
    ],
    imports: [
        ProjetoModule,
        PitModule,
        AtividadesModule,
        PortariaModule,
        CursosModule,
        AppRoutingModule,
        BrowserModule,
        SideNavOuterToolbarModule,
        SingleCardModule,
        FooterModule,
        ResetPasswordFormModule,
        CreateAccountFormModule,
        ChangePasswordFormModule,
        LoginFormModule,
        UnauthenticatedContentModule,
    ],
    providers: [
        AuthService,
        ScreenService,
        AppInfoService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AddHeaderInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

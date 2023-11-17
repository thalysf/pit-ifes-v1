import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {rotasDinamicas} from "../../app-routing.module";
import {AuthGuardService} from "../../shared/services";
import {ApoioEnsinoComponent} from "./apoio-ensino/apoio-ensino.component";
import {PesquisaComponent} from "./pesquisa/pesquisa.component";
import {ExtensaoComponent} from "./extensao/extensao.component";
import {OutrasComponent} from "./outras/outras.component";

const routes: Routes = [
    {
        path: 'atividades',
        children: [
            {
                path: 'apoioEnsino/novo',
                component: ApoioEnsinoComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'apoioEnsino/:id',
                component: ApoioEnsinoComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'pesquisa/novo',
                component: PesquisaComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'pesquisa/:id',
                component: PesquisaComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'extensao/novo',
                component: ExtensaoComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'extensao/:id',
                component: ExtensaoComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'outras/novo',
                component: OutrasComponent,
                canActivate: [AuthGuardService]
            },
            {
                path: 'outras/:id',
                component: OutrasComponent,
                canActivate: [AuthGuardService]
            },
            ...rotasDinamicas
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class AtividadesRoutingModule {
}

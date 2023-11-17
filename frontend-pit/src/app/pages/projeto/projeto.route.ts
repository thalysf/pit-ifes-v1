import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "../../shared/services";
import {ProjetoComponent} from "./projeto.component";
import {ImportarProjetosComponent} from "./importar-projetos/importar-projetos.component";

const routes: Routes = [
    {
        path: 'projetos/importar',
        component: ImportarProjetosComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'projetos/novo',
        component: ProjetoComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'projetos/:id',
        component: ProjetoComponent,
        canActivate: [AuthGuardService]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    providers: [AuthGuardService],
    exports: [RouterModule]
})
export class ProjetoRoute {
}

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from "../../shared/services";
import {ImportarCursosComponent} from "./importar-cursos/importar-cursos.component";

const routes: Routes = [
    {
        path: 'cursos/importar',
        component: ImportarCursosComponent,
        canActivate: [AuthGuardService]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class CursosRoutingModule {
}

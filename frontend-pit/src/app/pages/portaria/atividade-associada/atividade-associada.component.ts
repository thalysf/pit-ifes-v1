import {Component, Input, NgModule} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TipoAtividadeRotaEnum} from "../../../shared/enum/tipoAtividadeEnum";
import {DxDataGridModule} from "devextreme-angular";

@Component({
    selector: 'atividade-associada',
    templateUrl: './atividade-associada.component.html',
})
export class AtividadeAssociadaComponent {
    @Input() atividades!: any[];

    constructor(private router: Router, private route: ActivatedRoute) {
    }

    navigateToAtividade = ($action: any) => {
        const idAtividade = $action.row.data.idAtividade;
        const rotaAtividade = this.getRotaByTipoAtividade($action.row.data.tipoAtividade);
        this.router.navigate([`/${rotaAtividade}/${idAtividade}`], {relativeTo: this.route});
    }

    getRotaByTipoAtividade(tipoAtividade: string) {
        // @ts-ignore
        return TipoAtividadeRotaEnum[tipoAtividade];
    }
}

@NgModule({
    declarations: [
        AtividadeAssociadaComponent
    ],
    imports: [
        DxDataGridModule
    ],
    exports: [
        AtividadeAssociadaComponent
    ]
})
export class AtividadeAssociadaModule {
}

import {Component} from '@angular/core';
import {portariaResource} from "./portaria.resource";
import {TipoAtividadePortariaEnum} from "../../shared/enum/tipoPortariaEnum";

@Component({
    selector: 'app-portaria',
    templateUrl: './portaria.component.html',
})
export class PortariaComponent {
    resourcePortaria = portariaResource;
    id: any;
    entidade: any = {};
    entidadeOriginal: any;

    showAtividade(tipoAtividade: TipoAtividadePortariaEnum) {
        return tipoAtividade == TipoAtividadePortariaEnum.OUTRAS_ATIVIDADES;
    }
}

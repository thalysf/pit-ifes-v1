import {Component} from '@angular/core';
import {apoioEnsinoResource} from "./apoio-ensino.resource";
import {TipoAtividadePortariaEnum} from "../../../shared/enum/tipoPortariaEnum";
import {TipoDetalhamentoEnum} from "../../../shared/model/respostaAtividade";

@Component({
    selector: 'app-apoio-ensino',
    templateUrl: './apoio-ensino.component.html',
})
export class ApoioEnsinoComponent {
    resource = apoioEnsinoResource;
    id: any;
    entidade: any = {};
    entidadeOriginal: any;
    tipoAtividade = TipoAtividadePortariaEnum;
    tipoDetalhamentoProjeto = TipoDetalhamentoEnum.DETALHAMENTO_PROJETO;

    isVisible(isVisible?: (entity: any) => boolean) {
        if(isVisible == null || isVisible == undefined){
            return true;
        } else {
            return isVisible(this.entidadeOriginal);
        }
    }
}

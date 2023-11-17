import {Component} from '@angular/core';
import {pesquisaResource} from "./pesquisa.resource";
import {TipoAtividadePortariaEnum} from "../../../shared/enum/tipoPortariaEnum";

@Component({
    selector: 'app-pesquisa',
    templateUrl: './pesquisa.component.html',
})
export class PesquisaComponent {
    resource = pesquisaResource;
    id: any;
    entidade: any = {};
    entidadeOriginal: any;
    tipoAtividade = TipoAtividadePortariaEnum;

    isVisible(isVisible?: (entity: any) => boolean) {
        if (isVisible == null || isVisible == undefined) {
            return true;
        } else {
            return isVisible(this.entidadeOriginal);
        }
    }
}

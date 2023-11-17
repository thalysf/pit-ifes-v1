import {Component} from '@angular/core';
import {extensaoResource} from "./extensao.resource";
import {TipoAtividadePortariaEnum} from "../../../shared/enum/tipoPortariaEnum";

@Component({
    selector: 'app-extensao',
    templateUrl: './extensao.component.html',
})
export class ExtensaoComponent {
    resource = extensaoResource;
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

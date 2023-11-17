import {Component} from '@angular/core';
import {outrasResource} from "./outras.resource";

@Component({
    selector: 'app-outras',
    templateUrl: './outras.component.html',
})
export class OutrasComponent {
    resource = outrasResource;
    id: any;
    entidade: any = {};
    entidadeOriginal: any;

    isVisible(isVisible?: (entity: any) => boolean) {
        if (isVisible == null || isVisible == undefined) {
            return true;
        } else {
            return isVisible(this.entidadeOriginal);
        }
    }
}

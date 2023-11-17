import {Component} from '@angular/core';
import {projetoResource} from "./projeto.resource";

@Component({
    selector: 'projeto-component',
    templateUrl: 'projeto.component.html',
    styleUrls: []
})
export class ProjetoComponent {
    resource = projetoResource;
    id: string | null = null;
    entidade: any = {};
}

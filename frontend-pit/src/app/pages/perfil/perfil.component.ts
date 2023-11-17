import {Component, OnInit, ViewChild} from '@angular/core';
import { perfilResource } from './perfil.resource';
import {ActionToolbar} from "../../shared/components/toolbar/toolbar.component";
import {Observable} from "rxjs";
import {ApiService} from "../../shared/services/api.service";
import {AuthService} from "../../shared/services";
import {FormularioDinamicoComponent} from "../../shared/components/formulario-dinamico/formulario-dinamico.component";
import notify from "devextreme/ui/notify";

@Component({
    templateUrl: 'perfil.component.html'
})
export class PerfilComponent implements OnInit{
    @ViewChild('formulario', {static: false}) formulario!: FormularioDinamicoComponent;
    perfilResource;
    actionsPerfil: ActionToolbar[];
    entidade: any = {};

    constructor(private apiService: ApiService, private authService: AuthService) {
        this.perfilResource = perfilResource;

        this.actionsPerfil = [
            {
                text: 'Salvar',
                icon: 'save',
                type: 'success',
                onClick: this.salvar()
            }
        ]
    }

    ngOnInit(): void {
        this.detalharPerfil();
    }

    private detalharPerfil(){
        this.apiService.detail('servidores/usuario', this.authService.getUserId()!)
            .subscribe(response => {
                this.entidade = response;
            })
    }

    private salvar(){
        return new Observable(observer => {
            if(this.formulario.isValid()){
                const entitySave = this.formulario.getDadosFormularioSalvar();

                this.apiService.put('servidores', entitySave.idServidor, entitySave)
                    .subscribe(response => {
                        notify('Formulario salvo com sucesso', 'success', 2000);
                        observer.next();
                    }, error => {
                        observer.error();
                    })
            } else {
                observer.error();
            }
        });
    }
}

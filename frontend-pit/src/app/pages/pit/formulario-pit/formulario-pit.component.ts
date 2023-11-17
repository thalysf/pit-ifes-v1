import {Component, OnInit} from '@angular/core';
import {ColumnDataType, ConfigurationColumn} from "../../../shared/model/resource";
import {ActionToolbar} from "../../../shared/components/toolbar/toolbar.component";
import {Observable} from "rxjs";
import {AuthService} from "../../../shared/services";
import {ApiService} from "../../../shared/services/api.service";
import {Router} from "@angular/router";
import notify from "devextreme/ui/notify";

@Component({
  selector: 'app-formulario-pit',
  templateUrl: './formulario-pit.component.html',
  styleUrls: ['./formulario-pit.component.scss']
})
export class FormularioPitComponent implements OnInit{
    actions: ActionToolbar[] = [];
    loadPanelVisible = false;

    periodos: any[] = [];
    periodoSelecionado: number = 0;
    dataSelecionada: string | number | Date = new Date();

    constructor(
        private authService: AuthService,
        private apiService: ApiService,
        private router: Router
    ) {
        this.actions = [
            {
                type: 'success',
                icon: 'video',
                text: 'Iniciar PIT',
                onClick: this.iniciarPit()
            }
        ];

        this.periodos = [
            {id: 1, nome: '01 (primeiro)'},
            {id: 2, nome: '02 (segundo)'},
        ];
    }

    ngOnInit() {
        this.getPitEmAberto();
        const mesAtual = new Date().getMonth() + 1;
        if(mesAtual < 6){
            this.periodoSelecionado = 1;
        } else {
            this.periodoSelecionado = 2;
        }
    }

    getPitEmAberto(){
        this.loadPanelVisible = true;
        this.apiService.get(`pit/${this.authService.getUserId()}/em_aberto`).subscribe((response: any) => {
            this.loadPanelVisible = false;
            if(response['idPIT']){
                this.navigateToExecucao(response['idPIT']);
            }
        }, error => {
            this.loadPanelVisible = false;
        })
    }

    iniciarPit(){
        return new Observable(observer => {
            const periodo = this.periodoSelecionado + '/' + (this.dataSelecionada as Date).getFullYear();

            this.apiService.post(`pit/${this.authService.getUserId()}/inicializar`, {
                periodo: periodo
            }).subscribe((response: any) => {
                observer.next();
                this.navigateToExecucao(response['idPIT']);
            }, (error: any) => {
                notify(error.error.message, 'error', 2000);
                observer.error(error);
            })
        })
    }

    navigateToExecucao(idPit: string){
        this.router.navigate(['pits/responder/' + idPit], {replaceUrl: true});
    }
}

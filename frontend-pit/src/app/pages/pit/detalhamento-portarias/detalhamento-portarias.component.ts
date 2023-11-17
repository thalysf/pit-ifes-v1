import {Component, Input} from '@angular/core';
import {ApiService} from "../../../shared/services/api.service";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {forkJoin, Observable} from "rxjs";
import notify from "devextreme/ui/notify";
import {Portaria} from "../../../shared/model/portaria";
import {AuthService} from "../../../shared/services";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

export interface DetalhamentoPortariaComponente {
    cargaHorariaSemanal: HoraConfiguration,
    idDetalhamentoPortaria?: string,
    portaria: Portaria
}

export interface DetalhamentoPortaria {
    cargaHorariaSemanal: number,
    idDetalhamentoPortaria?: string,
    portaria: Portaria
}

export interface portariaEResposta {
    portaria: Portaria,
    resposta: DetalhamentoPortariaComponente
}

@Component({
    selector: 'app-detalhamento-portarias',
    templateUrl: './detalhamento-portarias.component.html',
    styleUrls: ['./detalhamento-portarias.component.scss']
})
export class DetalhamentoPortariasComponent {
    @Input() nomeAtividade!: string;
    @Input() idAtividade!: string;
    @Input() idPit!: string;
    @Input() totalHoras!: HoraConfiguration;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    showModalDetalhamento = false;
    portarias: Portaria[] = [];
    portariaERespostas: portariaEResposta[] = [];

    constructor(private apiService: ApiService, private authService: AuthService) {
    }

    abrirDetalhamentoAtividadePortaria() {
        this.showModalDetalhamento = true;
        this.listarPortariasAtividade();
    }

    private listarPortariasAtividade() {
        const observerPortarias = this.apiService.get(`atividades/${this.idAtividade}/portarias/${this.authService.getUserId()}`)

        const observerDetalhamento = this.apiService.get(`respostas/${this.idAtividade}/pit/${this.idPit}/detalhamento_portarias`)

        forkJoin([observerPortarias, observerDetalhamento])
            .subscribe(([respostaPortarias, respostaDetalhamentos]: any) => {
                this.portarias = respostaPortarias as Portaria[];
                const detalhamentos = respostaDetalhamentos as DetalhamentoPortaria[];

                this.portariaERespostas = [];
                this.portarias.forEach(portaria => {

                    let respostaDetalhamento = detalhamentos.find(detalhamento => detalhamento.portaria.idPortaria == portaria.idPortaria);

                    let respostaDetalhamentoComponente: DetalhamentoPortariaComponente;

                    if(!respostaDetalhamento) {
                        respostaDetalhamentoComponente = {
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
                            portaria: portaria
                        }
                    } else {
                        respostaDetalhamentoComponente = {
                            ...respostaDetalhamento,
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(respostaDetalhamento.cargaHorariaSemanal),
                        }
                    }

                    this.portariaERespostas.push({
                        portaria: {
                            ...portaria,
                            cargaHorariaMinima: portaria.cargaHorariaMinima as number,
                            cargaHorariaMaxima: portaria.cargaHorariaMaxima as number
                        },
                        resposta: respostaDetalhamentoComponente
                    })
                })
            })

    }

    salvar(fecharModal = true) {
        return new Observable(observer => {

            const respostaAtividadeSalvar = {
                atividade: {idAtividade: this.idAtividade},
                pit: {idPIT: this.idPit},
                detalhamentoPortarias: this.getDetalhamentos()
            };

            this.apiService.post('respostas', respostaAtividadeSalvar)
                .subscribe((response: any) => {
                    notify('Resposta salva com sucesso', 'success', 2000);
                    this.totalHoras = doubleToCargaHorariaComponent(response.cargaHorariaSemanal);
                    if(fecharModal) this.fecharModal();
                    this.obterTotalHoras();
                    observer.next();
                }, error => {
                    notify(error.error.message, 'error', 4000);
                    observer.error();
                })
        })
    }

    private fecharModal(){
        this.showModalDetalhamento = false;
    }

    private getDetalhamentos() {
        return this.portariaERespostas
            .filter(pr => stringHourFormatToDouble(pr.resposta.cargaHorariaSemanal.id) > 0)
            .map(pr => {
            return {
                cargaHorariaSemanal: stringHourFormatToDouble(pr.resposta.cargaHorariaSemanal.id),
                portaria: {idPortaria: pr.portaria.idPortaria}
            }
        })
    }

    changeCargaHorariaResposta(resposta: DetalhamentoPortariaComponente) {
        this.salvar(false)
            .subscribe();
    }
}

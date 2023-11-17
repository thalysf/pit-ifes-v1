import {Component, Input, OnInit} from '@angular/core';
import {
    DetalhamentoPortaria,
    DetalhamentoPortariaComponente,
    portariaEResposta
} from "../detalhamento-portarias/detalhamento-portarias.component";
import {forkJoin, Observable} from "rxjs";
import {Portaria} from "../../../shared/model/portaria";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import {ApiService} from "../../../shared/services/api.service";
import notify from "devextreme/ui/notify";

@Component({
    selector: 'app-responder-gestao',
    templateUrl: './responder-gestao.component.html',
    styleUrls: ['./responder-gestao.component.scss']
})
export class ResponderGestaoComponent implements OnInit {
    @Input() idPit!: string;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    portarias: Portaria[] = [];
    portariaERespostas: portariaEResposta[] = [];
    showLoadIndicator = false;

    constructor(private apiService: ApiService) {
    }

    ngOnInit(): void {
        this.listarPortariasAtividade();
    }

    private listarPortariasAtividade() {

        this.showLoadIndicator = true;

        const observerPortarias = this.apiService.get(`atividades/gestao/portarias/${this.idPit}`)

        const observerDetalhamento = this.apiService.get(`respostas/gestao/${this.idPit}/detalhamento_portarias`)

        forkJoin([observerPortarias, observerDetalhamento])
            .subscribe(([respostaPortarias, respostaDetalhamentos]: any) => {
                this.showLoadIndicator = false;

                this.portarias = respostaPortarias as Portaria[];
                const detalhamentos = respostaDetalhamentos as DetalhamentoPortaria[];

                this.portariaERespostas = [];
                this.portarias.forEach(portaria => {

                    let respostaDetalhamento = detalhamentos.find(detalhamento => detalhamento.portaria.idPortaria == portaria.idPortaria);
                    let detalhamentoPortariaComponente: DetalhamentoPortariaComponente;

                    if (!respostaDetalhamento) {
                        detalhamentoPortariaComponente = {
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
                            portaria: portaria
                        }
                    } else {
                        detalhamentoPortariaComponente = {
                            ...respostaDetalhamento,
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(respostaDetalhamento.cargaHorariaSemanal)
                        }
                    }

                    this.portariaERespostas.push({
                        portaria: {
                            ...portaria,
                            cargaHorariaMinima: portaria.cargaHorariaMinima as number,
                            cargaHorariaMaxima: portaria.cargaHorariaMaxima as number
                        },
                        resposta: detalhamentoPortariaComponente
                    })
                })
            })
    }

    changeCargaHorariaResposta(resposta: DetalhamentoPortariaComponente) {
        this.salvar().subscribe();
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

    salvar() {
        return new Observable(observer => {

            const respostaAtividadeSalvar = {
                pit: {idPIT: this.idPit},
                detalhamentoPortarias: this.getDetalhamentos()
            };

            this.apiService.post('respostas/gestao', respostaAtividadeSalvar)
                .subscribe((response: any) => {
                    this.obterTotalHoras();
                    notify('Resposta salva com sucesso', 'success', 2000);
                    observer.next();
                }, error => {
                    notify(error.error.message, 'error', 4000);
                    observer.error();
                })
        })
    }
}

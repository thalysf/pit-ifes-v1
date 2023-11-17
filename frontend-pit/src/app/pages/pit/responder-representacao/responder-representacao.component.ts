import {Component, Input} from '@angular/core';
import {Portaria} from "../../../shared/model/portaria";
import {
    DetalhamentoPortaria,
    DetalhamentoPortariaComponente,
    portariaEResposta
} from "../detalhamento-portarias/detalhamento-portarias.component";
import {ApiService} from "../../../shared/services/api.service";
import {forkJoin, Observable} from "rxjs";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import notify from "devextreme/ui/notify";

@Component({
  selector: 'app-responder-representacao',
  templateUrl: './responder-representacao.component.html',
  styleUrls: ['./responder-representacao.component.scss']
})
export class ResponderRepresentacaoComponent {
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

        const observerPortarias = this.apiService.get(`atividades/representacao/portarias/${this.idPit}`)

        const observerDetalhamento = this.apiService.get(`respostas/representacao/${this.idPit}/detalhamento_portarias`)

        forkJoin([observerPortarias, observerDetalhamento])
            .subscribe(([respostaPortarias, respostaDetalhamentos]: any) => {
                this.showLoadIndicator = false;

                this.portarias = respostaPortarias as Portaria[];
                const detalhamentos = respostaDetalhamentos as DetalhamentoPortaria[];

                this.portariaERespostas = [];
                this.portarias.forEach(portaria => {

                    let respostaDetalhamento = detalhamentos.find(detalhamento => detalhamento.portaria.idPortaria == portaria.idPortaria);
                    let respostaDetalhamentoComponente: DetalhamentoPortariaComponente;
                    if (!respostaDetalhamento) {
                        respostaDetalhamentoComponente = {
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
                            portaria: portaria
                        }
                    } else {
                        respostaDetalhamentoComponente = {
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
                        resposta: respostaDetalhamentoComponente
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

            this.apiService.post('respostas/representacao', respostaAtividadeSalvar)
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

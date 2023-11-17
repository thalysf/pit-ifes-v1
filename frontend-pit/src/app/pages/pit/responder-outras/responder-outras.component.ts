import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from "../../../shared/services/api.service";
import {forkJoin} from "rxjs";
import {Atividade, RespostaAtividade, RespostaAtividadeComponente} from "../../../shared/model/respostaAtividade";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import {AtividadeEResposta} from "../responder-apoio-ensino/responder-apoio-ensino.component";
import {AuthService} from "../../../shared/services";
import notify from "devextreme/ui/notify";
import {Portaria} from "../../../shared/model/portaria";

@Component({
    selector: 'app-responder-outras',
    templateUrl: './responder-outras.component.html',
    styleUrls: ['./responder-outras.component.scss']
})
export class ResponderOutrasComponent implements OnInit {
    @Input() idPit!: string;
    @Input() obterTotalHoras!: Function;
    @Input() pitAprovadoOuEmRevisao!: boolean;

    respostas: any[] = [{}];
    atividades: Atividade[] = [];
    atividadesERespostas: AtividadeEResposta[] = [];
    showLoadIndicator = false;


    constructor(private apiService: ApiService, private authService: AuthService) {
    }

    ngOnInit(): void {
        this.listarAtividades();
    }

    private listarAtividades() {
        this.showLoadIndicator = true;

        const observerAtividades = this.apiService.get('respostas/outras/atividades', {
            idUsuario: this.authService.getUserId(),
            idPit: this.idPit
        });

        const observerRespostas = this.apiService.get('respostas/outras', {idPit: this.idPit});

        forkJoin([observerAtividades, observerRespostas]).subscribe(
            ([resultadoAtividades, resultadoRespostas]: any) => {
                this.showLoadIndicator = false;
                const atividades: Atividade[] = resultadoAtividades;
                const respostas: RespostaAtividade[] = resultadoRespostas;

                atividades.forEach(atividade => {
                    let resposta = respostas.find(resposta => resposta.atividade?.idAtividade == atividade.idAtividade);
                    let respostaComponente: RespostaAtividadeComponente;
                    if (!resposta) {
                        respostaComponente = {
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(0),
                            atividade: atividade
                        }
                    } else {
                        respostaComponente = {
                            ...resposta,
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(resposta.cargaHorariaSemanal),
                        }
                    }

                    const portaria: Portaria = (atividade as any).portarias[0];

                    this.atividadesERespostas.push({
                        atividade: {
                            nomeAtividade: atividade.nomeAtividade,
                            tipoAtividade: atividade.tipoAtividade,
                            tipoDetalhamento: atividade.tipoDetalhamento,
                            idAtividade: atividade.idAtividade,
                            cargaHorariaMinima: portaria.cargaHorariaMinima as number,
                            cargaHorariaMaxima: portaria.cargaHorariaMaxima as number,
                            portarias: [portaria]
                        },
                        resposta: respostaComponente
                    })
                })
            }, () => {
                this.showLoadIndicator = false;
            }
        )
    }

    changeCargaHorariaResposta(resposta: RespostaAtividadeComponente) {
        this.salvarRespostaAtividade(resposta);
    }

    private salvarRespostaAtividade(resposta: RespostaAtividadeComponente) {
        const respostaAtividadeSalvar = {
            atividade: {
                idAtividade: resposta.atividade?.idAtividade,
            },
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
            pit: {idPIT: this.idPit},
            detalhamentoPortarias: [
                {
                    cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
                    portaria: resposta.atividade?.portarias![0]
                }
            ]
        };

        this.apiService.post('respostas/outras', respostaAtividadeSalvar)
            .subscribe(response => {
                this.obterTotalHoras();
                notify('Resposta salva com sucesso', 'success', 2000);
            }, error => {
                notify(error.error.message, 'error', 4000);
            })
    }
}

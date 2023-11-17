import {Component, Input, OnInit} from '@angular/core';
import {
    Atividade,
    RespostaAtividade,
    RespostaAtividadeComponente,
    TipoDetalhamentoEnum
} from "../../../shared/model/respostaAtividade";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import {ApiService} from "../../../shared/services/api.service";
import {AuthService} from "../../../shared/services";
import notify from "devextreme/ui/notify";
import {forkJoin} from "rxjs";


export interface AtividadeEResposta {
    atividade: Atividade,
    resposta: RespostaAtividadeComponente
}

@Component({
    selector: 'app-responder-apoio-ensino',
    templateUrl: './responder-apoio-ensino.component.html',
    styleUrls: ['./responder-apoio-ensino.component.scss', '../../../shared/components/formulario-dinamico/formulario-dinamico.component.css']
})
export class ResponderApoioEnsinoComponent implements OnInit {
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
        this.listarAtividadesResponder();
    }


    private listarAtividadesResponder() {
        this.showLoadIndicator = true;

        const observerAtividades = this.apiService.get('respostas/apoioEnsino/atividades', {
            idUsuario: this.authService.getUserId(),
            idPit: this.idPit
        });

        const observerRespostas = this.apiService.get('respostas/apoioEnsino', {idPit: this.idPit});

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
                            atividade: resposta.atividade,
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(resposta.cargaHorariaSemanal as number),
                            idRespostaAtividade: resposta.idRespostaAtividade,
                            pit: resposta.pit
                        }
                    }

                    this.atividadesERespostas.push({
                        atividade: {
                            nomeAtividade: atividade.nomeAtividade,
                            tipoAtividade: atividade.tipoAtividade,
                            tipoDetalhamento: atividade.tipoDetalhamento,
                            idAtividade: atividade.idAtividade,
                            cargaHorariaMinima: atividade.cargaHorariaMinima,
                            cargaHorariaMaxima: atividade.cargaHorariaMaxima
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

    detalharApenasCargaHoraria(tipoDetalhamento: TipoDetalhamentoEnum) {
        return tipoDetalhamento == TipoDetalhamentoEnum.NENHUM;
    }


    detalharProjeto(tipoDetalhamento: TipoDetalhamentoEnum) {
        return tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_PROJETO;
    }

    detalharPortaria(tipoDetalhamento: TipoDetalhamentoEnum) {
        return tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_PORTARIA;
    }

    detalharAula(tipoDetalhamento: TipoDetalhamentoEnum) {
        return tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_AULA;
    }

    detalharAluno(tipoDetalhamento: TipoDetalhamentoEnum) {
        return tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_ALUNO;
    }

    private salvarRespostaAtividade(resposta: RespostaAtividadeComponente) {
        const respostaAtividadeSalvar = {
            atividade: {
                idAtividade: resposta!.atividade!.idAtividade
            },
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
            idRespostaAtividade: resposta.idRespostaAtividade as string,
            pit: {idPIT: this.idPit as string}
        };

        this.apiService.post('respostas', respostaAtividadeSalvar)
            .subscribe(response => {
                notify('Resposta salva com sucesso', 'success', 2000);
                this.obterTotalHoras();
            }, error => {
                notify(error.error.message, 'error', 4000);
            })
    }
}

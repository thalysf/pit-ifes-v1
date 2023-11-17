import {Component, Input} from '@angular/core';
import {
    Atividade,
    RespostaAtividade,
    RespostaAtividadeComponente,
    TipoDetalhamentoEnum
} from "../../../shared/model/respostaAtividade";
import {ApiService} from "../../../shared/services/api.service";
import {AuthService} from "../../../shared/services";
import {forkJoin} from "rxjs";
import {doubleToCargaHorariaComponent, stringHourFormatToDouble} from "../../../shared/comum/cargaHoraria.resource";
import notify from "devextreme/ui/notify";
import {AtividadeEResposta} from "../responder-apoio-ensino/responder-apoio-ensino.component";

@Component({
    selector: 'app-responder-pesquisa',
    templateUrl: './responder-pesquisa.component.html',
    styleUrls: ['./responder-pesquisa.component.scss']
})
export class ResponderPesquisaComponent {
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

        const observerAtividades = this.apiService.get('respostas/pesquisa/atividades', {
            idUsuario: this.authService.getUserId(),
            idPit: this.idPit
        });

        const observerRespostas = this.apiService.get('respostas/pesquisa', {idPit: this.idPit});

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
                            cargaHorariaSemanal: doubleToCargaHorariaComponent(resposta.cargaHorariaSemanal)
                        }
                    }

                    this.atividadesERespostas.push({
                        atividade: {
                            nomeAtividade: atividade.nomeAtividade,
                            tipoAtividade: atividade.tipoAtividade,
                            tipoDetalhamento: atividade.tipoDetalhamento,
                            idAtividade: atividade.idAtividade,
                            cargaHorariaMinima: atividade.cargaHorariaMinima as number,
                            cargaHorariaMaxima: atividade.cargaHorariaMaxima as number
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

    salvarRespostaAtividade = (resposta: RespostaAtividadeComponente) => {
        const respostaAtividadeSalvar = {
            atividade: resposta.atividade,
            cargaHorariaSemanal: stringHourFormatToDouble(resposta.cargaHorariaSemanal.id),
            idRespostaAtividade: resposta.idRespostaAtividade,
            pit: {idPIT: this.idPit}
        };

        this.apiService.post('respostas/pesquisa', respostaAtividadeSalvar)
            .subscribe(response => {
                this.obterTotalHoras();
                notify('Resposta salva com sucesso', 'success', 2000);
            }, error => {
                notify(error.error.message, 'error', 4000);
            })
    }
}

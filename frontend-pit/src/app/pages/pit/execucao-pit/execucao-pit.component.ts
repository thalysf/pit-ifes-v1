import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TipoAtividadeTabsEnum} from "../../../shared/enum/tipoAtividadeEnum";
import {Location} from "@angular/common";
import {doubleToDateStringFormat} from "../../../shared/comum/cargaHoraria.resource";
import {ApiService} from "../../../shared/services/api.service";
import notify from "devextreme/ui/notify";


export interface Tab {
    id: TipoAtividadeTabsEnum;
    text: string;
    icon?: string;
}

@Component({
    selector: 'app-execucao-pit',
    templateUrl: './execucao-pit.component.html',
    styleUrls: [
        './execucao-pit.component.scss',
        '../../../shared/components/formulario-dinamico/formulario-dinamico.component.css',
        '../../../shared/components/toolbar/toolbar.component.css'
    ]
})
export class ExecucaoPitComponent implements OnInit {

    id!: string;
    tabs: Tab[] = [];
    selectedTab!: Tab;
    backOption: any;
    enviarRevisaoOption: any;
    totalHorasPit = doubleToDateStringFormat(0);
    pitAprovadoOuEmRevisao = false;
    pit: any = null;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private location: Location,
        private apiService: ApiService,
    ) {
        this.backOption = {
            onClick: this.backFunction,
            icon: 'back',
            classList: 'btn-back'
        }

        this.tabs = [
            {
                id: TipoAtividadeTabsEnum.AULA,
                text: TipoAtividadeTabsEnum.AULA
            },
            {
                id: TipoAtividadeTabsEnum.APOIO_AO_ENSINO,
                text: TipoAtividadeTabsEnum.APOIO_AO_ENSINO
            },
            {
                id: TipoAtividadeTabsEnum.MEDIACAO_PEDAGOGICA,
                text: TipoAtividadeTabsEnum.MEDIACAO_PEDAGOGICA
            },
            {
                id: TipoAtividadeTabsEnum.ATIVIDADE_PESQUISA,
                text: TipoAtividadeTabsEnum.ATIVIDADE_PESQUISA
            },
            {
                id: TipoAtividadeTabsEnum.ATIVIDADE_EXTENSAO,
                text: TipoAtividadeTabsEnum.ATIVIDADE_EXTENSAO
            },
            {
                id: TipoAtividadeTabsEnum.ATIVIDADE_GESTAO,
                text: TipoAtividadeTabsEnum.ATIVIDADE_GESTAO
            },
            {
                id: TipoAtividadeTabsEnum.ATIVIDADE_REPRESENTACAO,
                text: TipoAtividadeTabsEnum.ATIVIDADE_REPRESENTACAO
            },
            {
                id: TipoAtividadeTabsEnum.OUTRAS_ATIVIDADES,
                text: TipoAtividadeTabsEnum.OUTRAS_ATIVIDADES
            },
        ];
    }

    ngOnInit() {
        this.id = this.route.snapshot.paramMap.get('id')!;
        if (!this.id) throw new Error('Erro no ID do componente execucao-pit');

        this.selecionarTabByUrl();
        this.obterTotalHoras();
        this.obterDadosPit();
    }

    backFunction = () => {
        this.location.back();
    }

    configurarBotaoEnviarRevisao(){
        let texto = 'Enviar para revisão';
        if(this.pit.aprovado) texto = 'Aprovado';
        else if (this.pit.emRevisao) texto = 'Em revisão';

        this.enviarRevisaoOption = {
            onClick: ($button: any) => this.enviarRevisao($button),
            icon: 'arrowright',
            type: 'success',
            text: texto,
            disabled: this.pitAprovadoOuEmRevisao
        }
    }

    enviarRevisao = (button: any) => {
        const icon = button.component.option('icon');
        button.component.option({disabled: true, icon: 'zmdi zmdi-spinner zmdi-hc-spin'});

        this.apiService.post(`pit/${this.id}/enviar_revisao`, {})
            .subscribe(response => {
                this.obterDadosPit();
                this.baixarPit(this.pit.idPIT, this.pit.periodo);
                button.component.option({disabled: false, icon: icon});
            }, e => {
                notify(e.error.message, 'error', 4000);
                button.component.option({disabled: false, icon: icon});
            })
    }

    baixarPit = (idPit: string, periodo: string) => {
        this.apiService.getFile(`relatorios/${idPit}`)
            .subscribe((response: any) => {
                const a = document.createElement('a');
                document.body.appendChild(a);
                const blob: any = new Blob([response], { type: 'octet/stream' });
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = `${periodo}.xls`;
                a.click();
                window.URL.revokeObjectURL(url);
            })
    }

    selecionarTabByUrl() {
        let atividade = this.route.snapshot.queryParams['atividade'];
        if (!atividade) atividade = TipoAtividadeTabsEnum.AULA;

        const tabSelecionada = this.tabs.find(tab => tab.id == atividade);
        if (tabSelecionada) this.selectedTab = tabSelecionada;
    }

    obterTotalHoras = () => {
        this.apiService.get(`pit/${this.id}/totalHoras`)
            .subscribe(response => {
                this.totalHorasPit = doubleToDateStringFormat(response as number);
            })
    }

    obterDadosPit = () => {
        this.apiService.get(`pit/${this.id}`)
            .subscribe((response: any) => {
                this.pit = response;
                if (response.aprovado == false && response.emRevisao == false) {
                    this.pitAprovadoOuEmRevisao = false;
                } else {
                    this.pitAprovadoOuEmRevisao = true;
                }
                this.configurarBotaoEnviarRevisao();
            })
    }

    selectTab(event: any) {
        this.location.replaceState(`pits/responder/${this.id}?atividade=${this.selectedTab.id}`);
    }

    isAbaAulas() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.AULA;
    }

    isAbaMediacao() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.MEDIACAO_PEDAGOGICA;
    }

    isAbaApoioEnsino() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.APOIO_AO_ENSINO;
    }

    isAbaPesquisa() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.ATIVIDADE_PESQUISA;
    }

    isAbaExtensao() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.ATIVIDADE_EXTENSAO;
    }

    isAbaGestao() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.ATIVIDADE_GESTAO;
    }

    isAbaRepresentacao() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.ATIVIDADE_REPRESENTACAO;
    }

    isAbaOutras() {
        return this.selectedTab.id == TipoAtividadeTabsEnum.OUTRAS_ATIVIDADES;
    }
}

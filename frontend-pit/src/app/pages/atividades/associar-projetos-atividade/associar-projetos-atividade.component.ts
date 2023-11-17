import {Component, Input, ViewChild} from '@angular/core';
import {
    ActionsTabela,
    TabelaListagemComponent
} from "../../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {projetoResource} from "../../projeto/projeto.resource";
import {Observable} from "rxjs";
import {ApiService} from "../../../shared/services/api.service";
import notify from "devextreme/ui/notify";
import {confirm} from "devextreme/ui/dialog";
import {TipoAtividadePortariaEnum} from "../../../shared/enum/tipoPortariaEnum";

@Component({
    selector: 'associar-projetos-atividade',
    templateUrl: './associar-projetos-atividade.component.html',
})
export class AssociarProjetosAtividadeComponent {
    @ViewChild('tabelaListagem', {static: false}) tabelaListagem!: TabelaListagemComponent;
    @ViewChild('tabelaListagemModal', {static: false}) tabelaListagemModal!: TabelaListagemComponent;

    @Input() idAtividade!: string;
    @Input() tipoAtividade!: TipoAtividadePortariaEnum;

    colunasProjetos = projetoResource.colunas;
    actions: ActionsTabela[] = [];
    showModal = false;

    constructor(private apiService: ApiService) {
        this.actions = [
            {
                type: 'success',
                icon: 'link',
                mode: 'batch',
                text: '',
                hint: 'Associar projetos',
                onClick: this.abrirModalAssociarProjetos
            },
            {
                type: 'success',
                icon: 'trash',
                text: '',
                hint: 'Remover projeto',
                mode: 'single',
                onClick: this.removerProejtoAssociado
            }
        ]
    }


    getUrlProjetosAtividade() {
        return `atividades/${this.idAtividade}/projetos`;
    }

    salvar() {
        return new Observable(observer => {
            const projetoId = this.tabelaListagemModal.getSelectedIds()[0];

            if(!projetoId){
                notify('Primeiramente selecione um projeto', 'error', 2000);
                observer.error('Primeiramente selecione um projeto');
            } else {
                this.apiService.post(`atividades/${this.idAtividade}/projetos/${projetoId}`, {})
                    .subscribe(response => {
                        this.closeModalAssociarProjetos();
                        this.tabelaListagem.listarEntidades();
                        observer.next();
                    }, error =>{
                        notify(error.error.message, 'error', 4000);
                        observer.error();
                    })
            }
        })
    }

    abrirModalAssociarProjetos = () => {
        this.showModal = true;
        this.tabelaListagemModal.listarEntidades();
    }

    closeModalAssociarProjetos = () => {
        this.showModal = false;
    }

    removerProejtoAssociado = (event: any) => {
        const projetoId = event.row.data.idProjeto;

        confirm('Tem certeza que deseja remover o projeto selecionado?', 'Confirmar Exclusão ' + event.row.data.numeroCadastro)
            .then(confirmado => {
                if (confirmado) {
                    this.apiService.delete(`atividades/${this.idAtividade}/projetos/${projetoId}`, '')
                        .subscribe(response => {
                            this.tabelaListagem.listarEntidades();
                        }, error => {
                            notify(error.error.message, 'error', 4000);
                        });
                }
            })
    }

    getUrlModalAssociacao() {
        return `atividades/${this.idAtividade}/${this.tipoAtividade}/projetos_nao_associados`;
    }
}

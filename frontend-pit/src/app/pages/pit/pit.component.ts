import {Component} from '@angular/core';
import {ColumnDataType, ConfigurationColumn} from "../../shared/model/resource";
import {ActionsTabela} from "../../shared/components/table-list/tabela-listagem/tabela-listagem.component";
import {ApiService} from "../../shared/services/api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../shared/services";

@Component({
    selector: 'app-pit',
    templateUrl: './pit.component.html',
    styleUrls: ['./pit.component.scss']
})
export class PitComponent {
    colunas: ConfigurationColumn[] = [];
    actions: ActionsTabela[] = [];

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private authService: AuthService,
        private apiService: ApiService
    ) {
        this.colunas = [
            {
                label: 'Período',
                field: 'periodo',
            },
            {
                field: 'emRevisao',
                label: 'Em Revisão',
                type: ColumnDataType.Checkbox
            },
            {
                field: 'aprovado',
                label: 'Aprovado',
                type: ColumnDataType.Checkbox
            }
        ];

        this.actions = [
            {
                text: '',
                icon: 'plus',
                mode: 'batch',
                type: 'success',
                hint: 'Iniciar novo PIT',
                onClick: this.navigateNewPit
            },
            {
                text: 'Download',
                icon: 'download',
                mode: 'single',
                type: 'success',
                hint: 'Baixar PIT',
                onClick: this.baixarPit,
                isVisible: (event: any) => event.row.data.emRevisao == true || event.row.data.aprovado == true
            },
            {
                text: '',
                icon: 'video',
                mode: 'single',
                type: 'success',
                hint: 'Continuar PIT',
                onClick: this.navigateExecucaoPit,
                isVisible: (event: any) => event.row.data.aprovado == false
            },
        ]
    }

    navigateNewPit = () => {
        this.router.navigate(['new'], {relativeTo: this.route});
    }

    navigateExecucaoPit = ($event: any) => {
        const idPit = $event.row.data.idPIT;
        this.router.navigate([`responder/${idPit}`], {relativeTo: this.route});
    }

    baixarPit = ($event: any) => {
        const idPit = $event.row.data.idPIT;

        this.apiService.getFile(`relatorios/${idPit}`)
            .subscribe((response: any) => {
                const a = document.createElement('a');
                document.body.appendChild(a);
                const blob: any = new Blob([response], { type: 'octet/stream' });
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = `${$event.row.data.periodo}.xls`;
                a.click();
                window.URL.revokeObjectURL(url);
            })
    }

    getUrlListarPit = () => {
        return `pit/?idUsuario=${this.authService.getUserId()}`;
    }
}

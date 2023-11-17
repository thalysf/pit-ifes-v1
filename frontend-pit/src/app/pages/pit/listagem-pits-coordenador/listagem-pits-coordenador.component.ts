import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../shared/services";
import {ApiService} from "../../../shared/services/api.service";
import {ColumnDataType, ConfigurationColumn} from "../../../shared/model/resource";
import {ActionsTabela} from "../../../shared/components/table-list/tabela-listagem/tabela-listagem.component";

@Component({
    selector: 'app-listagem-pits-coordenador',
    templateUrl: './listagem-pits-coordenador.component.html',
    styleUrls: ['./listagem-pits-coordenador.component.scss']
})
export class ListagemPitsCoordenadorComponent implements OnInit {
    colunas: ConfigurationColumn[] = [];
    actions: ActionsTabela[] = [];
    status!: 'emRevisao' | 'aprovado';

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
                field: 'professor.nome',
                label: 'Professor',
            },
            {
                field: 'dataEntrega',
                label: 'Ultima modificação',
                type: ColumnDataType.Date,
                dateType: "date"
            },
        ];

        this.actions = [
            {
                text: 'Download',
                icon: 'download',
                mode: 'single',
                type: 'success',
                hint: 'Baixar PIT',
                onClick: this.baixarPit,
            },
            {
                text: 'Revisar',
                icon: 'video',
                mode: 'single',
                type: 'success',
                hint: 'Revisar PIT',
                onClick: this.navigateToRevisar,
            },
        ]
    }

    ngOnInit(): void {
        const status = this.route.snapshot?.routeConfig?.path?.split('/').pop();
        if (status != null) {
            this.status = status as unknown as 'emRevisao' | 'aprovado';
        }
    }

    baixarPit = ($event: any) => {
        const idPit = $event.row.data.idPIT;
        const periodo = $event.row.data.periodo;
        const professor = $event.row.data.professor.nome;

        this.apiService.getFile(`relatorios/${idPit}`)
            .subscribe((response: any) => {
                const a = document.createElement('a');
                document.body.appendChild(a);
                const blob: any = new Blob([response], {type: 'octet/stream'});
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = `${periodo}-${professor}.xls`;
                a.click();
                window.URL.revokeObjectURL(url);
            })
    }

    navigateToRevisar = ($event: any) => {
        const idPit = $event.row.data.idPIT;
        console.log(idPit)
        this.router.navigate([`/pits/revisar/${idPit}`], {relativeTo: this.route});
    }

    getUrlListarPit() {
        if (this.status == 'emRevisao') return 'pit/emRevisao';
        return 'pit/aprovados';
    }

    getTitulo() {
        if (this.status == 'emRevisao') return 'Listagem dos PIT\'s para revisar';
        return 'Listagem dos PIT\'s aprovados';
    }
}

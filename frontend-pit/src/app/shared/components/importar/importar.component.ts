import {Component, Input, NgModule} from '@angular/core';
import {Observable} from "rxjs";
import {ApiService} from "../../../shared/services/api.service";
import notify from "devextreme/ui/notify";
import {UploadButtonModule} from "../upload-button/upload-button.module";
import {ToolbarModule} from "../toolbar/toolbar.component";
import {CommonModule} from "@angular/common";


@Component({
  selector: 'app-importar',
  templateUrl: './importar.component.html',
  styleUrls: ['./importar.component.scss']
})
export class ImportarComponent {
    actions: any[];
    errorMessage = '';

    @Input() tituloToolbar!: string;
    @Input() tipoTemplateDownload!: string;
    @Input() rotaImportarEntidade!: string;
    @Input() nomeArquivoModelo!: string;

    constructor(private apiService: ApiService) {
        this.actions = [
            {
                text: 'Baixar modelo',
                icon: 'download',
                type: 'default',
                onClick: this.baixarModelo()
            }
        ]
    }

    baixarModelo = () => {
        return new Observable(observer => {
            this.apiService.getFile('batch/template/download', {
                template: this.tipoTemplateDownload
            }).subscribe(response => {
                const a = document.createElement('a');
                document.body.appendChild(a);
                const blob: any = new Blob([response], { type: 'octet/stream' });
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = `${this.nomeArquivoModelo}.xls`;
                a.click();
                window.URL.revokeObjectURL(url);
                observer.next();
            }, e => {
                notify(e.error.message, 'error', 4000);
                observer.error();
            })
        })
    }
}
@NgModule({
    imports: [
        CommonModule,
        UploadButtonModule,
        ToolbarModule
    ],
    declarations: [ImportarComponent],
    exports: [ImportarComponent]
})
export class ImportarModule {
}

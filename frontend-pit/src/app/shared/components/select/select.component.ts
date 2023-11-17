import {Component, EventEmitter, Input, NgModule, OnDestroy, OnInit, Output} from "@angular/core";
import {ApiService} from "../../services/api.service";
import {SelectConfiguration} from "../../model/resource";
import {DxSelectBoxModule} from "devextreme-angular";
import {FormControl} from "@angular/forms";


@Component({
    selector: 'select-padrao',
    templateUrl: 'select.component.html',
})
export class SelectComponent implements OnInit, OnDestroy {
    private control: FormControl | null = null;

    @Input() configuration!: SelectConfiguration;
    @Input() selected: any = null;
    @Input() required?: boolean = false;

    @Output() selectedChanged = new EventEmitter<any>();

    entidades: any[] = [];
    apiSubscription: any;

    constructor(
        private apiService: ApiService
    ) {
    }

    ngOnInit(): void {
        this.listarEntidades();
    }

    ngOnDestroy(): void {
        this.entidades = [];
        if (this.apiSubscription) this.apiSubscription.unsubscribe();
    }

    public listarEntidades() {
        if (this.configuration.route)
            this.apiSubscription = this.apiService.get(this.configuration.route).subscribe(
                response => {
                    this.entidades = response as any[];
                }
            );
    }

    onSelectionChanged() {
        this.selectedChanged.emit(this.selected);
        this.control?.setValue(this.selected);
    }

}

@NgModule({
    imports: [
        DxSelectBoxModule
    ],
    declarations: [SelectComponent],
    exports: [
        SelectComponent
    ]
})
export class SelectModule {
}

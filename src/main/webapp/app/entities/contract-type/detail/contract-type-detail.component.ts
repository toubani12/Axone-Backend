import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IContractType } from '../contract-type.model';

@Component({
  standalone: true,
  selector: 'jhi-contract-type-detail',
  templateUrl: './contract-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ContractTypeDetailComponent {
  contractType = input<IContractType | null>(null);

  previousState(): void {
    window.history.back();
  }
}

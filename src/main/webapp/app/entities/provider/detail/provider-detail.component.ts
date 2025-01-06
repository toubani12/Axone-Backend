import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProvider } from '../provider.model';

@Component({
  standalone: true,
  selector: 'jhi-provider-detail',
  templateUrl: './provider-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProviderDetailComponent {
  provider = input<IProvider | null>(null);

  previousState(): void {
    window.history.back();
  }
}

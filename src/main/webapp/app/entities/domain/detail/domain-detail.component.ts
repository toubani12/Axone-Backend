import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDomain } from '../domain.model';

@Component({
  standalone: true,
  selector: 'jhi-domain-detail',
  templateUrl: './domain-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DomainDetailComponent {
  domain = input<IDomain | null>(null);

  previousState(): void {
    window.history.back();
  }
}

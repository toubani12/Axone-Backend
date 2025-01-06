import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICriteria } from '../criteria.model';

@Component({
  standalone: true,
  selector: 'jhi-criteria-detail',
  templateUrl: './criteria-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CriteriaDetailComponent {
  criteria = input<ICriteria | null>(null);

  previousState(): void {
    window.history.back();
  }
}

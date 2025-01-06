import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICandidate } from '../candidate.model';

@Component({
  standalone: true,
  selector: 'jhi-candidate-detail',
  templateUrl: './candidate-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CandidateDetailComponent {
  candidate = input<ICandidate | null>(null);

  previousState(): void {
    window.history.back();
  }
}

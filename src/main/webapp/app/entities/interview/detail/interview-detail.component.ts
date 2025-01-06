import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInterview } from '../interview.model';

@Component({
  standalone: true,
  selector: 'jhi-interview-detail',
  templateUrl: './interview-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InterviewDetailComponent {
  interview = input<IInterview | null>(null);

  previousState(): void {
    window.history.back();
  }
}

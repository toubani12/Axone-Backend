import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICustomQuestion } from '../custom-question.model';

@Component({
  standalone: true,
  selector: 'jhi-custom-question-detail',
  templateUrl: './custom-question-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomQuestionDetailComponent {
  customQuestion = input<ICustomQuestion | null>(null);

  previousState(): void {
    window.history.back();
  }
}

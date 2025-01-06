import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVEmployment } from '../tech-cv-employment.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-employment-detail',
  templateUrl: './tech-cv-employment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVEmploymentDetailComponent {
  techCVEmployment = input<ITechCVEmployment | null>(null);

  previousState(): void {
    window.history.back();
  }
}

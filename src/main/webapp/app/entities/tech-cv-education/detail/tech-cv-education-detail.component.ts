import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVEducation } from '../tech-cv-education.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-education-detail',
  templateUrl: './tech-cv-education-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVEducationDetailComponent {
  techCVEducation = input<ITechCVEducation | null>(null);

  previousState(): void {
    window.history.back();
  }
}

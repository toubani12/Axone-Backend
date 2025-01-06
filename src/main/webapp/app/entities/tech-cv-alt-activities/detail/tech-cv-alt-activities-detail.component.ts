import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-alt-activities-detail',
  templateUrl: './tech-cv-alt-activities-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVAltActivitiesDetailComponent {
  techCVAltActivities = input<ITechCVAltActivities | null>(null);

  previousState(): void {
    window.history.back();
  }
}

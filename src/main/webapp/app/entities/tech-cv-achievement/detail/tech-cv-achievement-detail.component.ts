import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVAchievement } from '../tech-cv-achievement.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-achievement-detail',
  templateUrl: './tech-cv-achievement-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVAchievementDetailComponent {
  techCVAchievement = input<ITechCVAchievement | null>(null);

  previousState(): void {
    window.history.back();
  }
}

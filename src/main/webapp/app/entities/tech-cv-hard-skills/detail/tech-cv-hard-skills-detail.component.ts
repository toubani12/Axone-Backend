import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-hard-skills-detail',
  templateUrl: './tech-cv-hard-skills-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVHardSkillsDetailComponent {
  techCVHardSkills = input<ITechCVHardSkills | null>(null);

  previousState(): void {
    window.history.back();
  }
}

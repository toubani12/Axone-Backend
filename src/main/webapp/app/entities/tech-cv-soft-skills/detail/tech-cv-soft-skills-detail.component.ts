import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-soft-skills-detail',
  templateUrl: './tech-cv-soft-skills-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVSoftSkillsDetailComponent {
  techCVSoftSkills = input<ITechCVSoftSkills | null>(null);

  previousState(): void {
    window.history.back();
  }
}

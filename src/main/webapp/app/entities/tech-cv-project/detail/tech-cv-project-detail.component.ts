import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITechCVProject } from '../tech-cv-project.model';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-project-detail',
  templateUrl: './tech-cv-project-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TechCVProjectDetailComponent {
  techCVProject = input<ITechCVProject | null>(null);

  previousState(): void {
    window.history.back();
  }
}

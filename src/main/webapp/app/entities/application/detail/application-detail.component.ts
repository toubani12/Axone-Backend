import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IApplication } from '../application.model';

@Component({
  standalone: true,
  selector: 'jhi-application-detail',
  templateUrl: './application-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ApplicationDetailComponent {
  application = input<IApplication | null>(null);

  previousState(): void {
    window.history.back();
  }
}

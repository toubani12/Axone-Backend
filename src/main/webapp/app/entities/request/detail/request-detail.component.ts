import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRequest } from '../request.model';

@Component({
  standalone: true,
  selector: 'jhi-request-detail',
  templateUrl: './request-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RequestDetailComponent {
  request = input<IRequest | null>(null);

  previousState(): void {
    window.history.back();
  }
}

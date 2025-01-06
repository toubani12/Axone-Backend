import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAdmin } from '../admin.model';

@Component({
  standalone: true,
  selector: 'jhi-admin-detail',
  templateUrl: './admin-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AdminDetailComponent {
  admin = input<IAdmin | null>(null);

  previousState(): void {
    window.history.back();
  }
}

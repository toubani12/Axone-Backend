import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAppTestType } from '../app-test-type.model';

@Component({
  standalone: true,
  selector: 'jhi-app-test-type-detail',
  templateUrl: './app-test-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppTestTypeDetailComponent {
  appTestType = input<IAppTestType | null>(null);

  previousState(): void {
    window.history.back();
  }
}

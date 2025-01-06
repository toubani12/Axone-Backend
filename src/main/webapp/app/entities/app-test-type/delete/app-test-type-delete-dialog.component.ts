import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAppTestType } from '../app-test-type.model';
import { AppTestTypeService } from '../service/app-test-type.service';

@Component({
  standalone: true,
  templateUrl: './app-test-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AppTestTypeDeleteDialogComponent {
  appTestType?: IAppTestType;

  protected appTestTypeService = inject(AppTestTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appTestTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

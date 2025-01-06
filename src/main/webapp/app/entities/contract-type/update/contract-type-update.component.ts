import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IContractType } from '../contract-type.model';
import { ContractTypeService } from '../service/contract-type.service';
import { ContractTypeFormService, ContractTypeFormGroup } from './contract-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-type-update',
  templateUrl: './contract-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractTypeUpdateComponent implements OnInit {
  isSaving = false;
  contractType: IContractType | null = null;

  applicationsSharedCollection: IApplication[] = [];

  protected contractTypeService = inject(ContractTypeService);
  protected contractTypeFormService = inject(ContractTypeFormService);
  protected applicationService = inject(ApplicationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractTypeFormGroup = this.contractTypeFormService.createContractTypeFormGroup();

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractType }) => {
      this.contractType = contractType;
      if (contractType) {
        this.updateForm(contractType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractType = this.contractTypeFormService.getContractType(this.editForm);
    if (contractType.id !== null) {
      this.subscribeToSaveResponse(this.contractTypeService.update(contractType));
    } else {
      this.subscribeToSaveResponse(this.contractTypeService.create(contractType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contractType: IContractType): void {
    this.contractType = contractType;
    this.contractTypeFormService.resetForm(this.editForm, contractType);

    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      ...(contractType.applications ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
            applications,
            ...(this.contractType?.applications ?? []),
          ),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }
}

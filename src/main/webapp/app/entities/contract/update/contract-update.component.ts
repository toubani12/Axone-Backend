import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITemplate } from 'app/entities/template/template.model';
import { TemplateService } from 'app/entities/template/service/template.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { TemplateContractType } from 'app/entities/enumerations/template-contract-type.model';
import { ContractStatus } from 'app/entities/enumerations/contract-status.model';
import { ContractService } from '../service/contract.service';
import { IContract } from '../contract.model';
import { ContractFormService, ContractFormGroup } from './contract-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  templateContractTypeValues = Object.keys(TemplateContractType);
  contractStatusValues = Object.keys(ContractStatus);

  templatesCollection: ITemplate[] = [];
  candidatesCollection: ICandidate[] = [];
  recruitersSharedCollection: IRecruiter[] = [];
  employersSharedCollection: IEmployer[] = [];
  applicationsSharedCollection: IApplication[] = [];

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected templateService = inject(TemplateService);
  protected candidateService = inject(CandidateService);
  protected recruiterService = inject(RecruiterService);
  protected employerService = inject(EmployerService);
  protected applicationService = inject(ApplicationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  compareTemplate = (o1: ITemplate | null, o2: ITemplate | null): boolean => this.templateService.compareTemplate(o1, o2);

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  compareRecruiter = (o1: IRecruiter | null, o2: IRecruiter | null): boolean => this.recruiterService.compareRecruiter(o1, o2);

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.templatesCollection = this.templateService.addTemplateToCollectionIfMissing<ITemplate>(
      this.templatesCollection,
      contract.template,
    );
    this.candidatesCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesCollection,
      contract.candidate,
    );
    this.recruitersSharedCollection = this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(
      this.recruitersSharedCollection,
      contract.recruiter,
    );
    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      contract.employer,
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      contract.application,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.templateService
      .query({ filter: 'contract-is-null' })
      .pipe(map((res: HttpResponse<ITemplate[]>) => res.body ?? []))
      .pipe(
        map((templates: ITemplate[]) =>
          this.templateService.addTemplateToCollectionIfMissing<ITemplate>(templates, this.contract?.template),
        ),
      )
      .subscribe((templates: ITemplate[]) => (this.templatesCollection = templates));

    this.candidateService
      .query({ filter: 'contract-is-null' })
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, this.contract?.candidate),
        ),
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesCollection = candidates));

    this.recruiterService
      .query()
      .pipe(map((res: HttpResponse<IRecruiter[]>) => res.body ?? []))
      .pipe(
        map((recruiters: IRecruiter[]) =>
          this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(recruiters, this.contract?.recruiter),
        ),
      )
      .subscribe((recruiters: IRecruiter[]) => (this.recruitersSharedCollection = recruiters));

    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) =>
          this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.contract?.employer),
        ),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, this.contract?.application),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }
}

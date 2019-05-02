$(() => {
	let container = $("#toggleRegLog");
		setContent(container);
	$("a").on('click', function(e){
		e.preventDefault();
		
		
		$("#register").on("click", function()
			{
				let isThere = $(this).attr("class");
				if(isThere.indexOf("active") >= 0)
				{
					console.log("yes");
				}
				else
				{

					let isThere = $("#login").attr("class");
					if(isThere.indexOf("active") >= 0)
					{
						$("#login").removeClass("active");
						$(this).addClass("active");
						setContent(container);
					}
				}
			});
		$("#login").on("click", function()
			{
				let isThere = $(this).attr("class");
				if(isThere.indexOf("active") >= 0)
				{
					console.log("yes");
				}

				else
				{

					let isThere = $("#register").attr("class");
					if(isThere.indexOf("active") >= 0)
					{
						$("#register").removeClass("active");
						$(this).addClass("active");
						setContent(container);
					}
				}
			});

	}); ///////on


});

const setContent = (container) =>{

		let isRegActive = $("#register").attr("class");
		let isLogActive = $("#login").attr("class");
		if(isRegActive.indexOf("active") >= 0)
			{
				register(container);
			}
		else if(isLogActive.indexOf("active") >= 0)
			{
				login(container);
			}

}

const login = (container)=>
{
	container.empty();
    container.append(`
	    	<section id="forms">
	    		<div class="row">
				<div class="col-12 col-md-6">
					<div class="card">
						<div class="card-header"></div>
						<div class="card-body">
							<form>
								<fieldset>
									<div class="row">
										<div class="col-12 col-lg-6">
											<label for="loginEmail">Email Address:</label>
											<input type="email" id="loginEmail" class="form-control" placeholder="name@email.com" name="email">
										</div>
										<div class="col-12 col-lg-6">
											<label for="loginPass">Password:</label>
											<input type="password" id="loginPass" class="form-control" placeholder="******" name="pass">
										</div>
									</div>
									<div class="row mt-3">
										<div class="col-12">
											<button type="submit" class="btn btn-dark">Login <i class="fa fa-angle-right"></i></button>
										</div>
									</div>
								</fieldset>
							</form>
						</div>
					</div>
				</div>
	    	</section>

    	`);
}

const register = (container) =>
{
	container.empty();
    container.append(`
    	<section id="forms">
	    	<div class="row">
		    	<div class="col-12 col-md-6 mt-3 mt-md-0">
						<div class="card">
							<div class="card-header"></div>
							<div class="card-body">
								<form>
									<fieldset>
										<div class="row">
											<div class="col-12 col-lg-6">
												<label for="regName">First Name:</label>
												<input type="text" id="regName" class="form-control" placeholder="John" name="fname">
											</div>
											<div class="col-12 col-lg-6">
												<label for="regSurname">Last Name:</label>
												<input type="text" id="regSurname" class="form-control" placeholder="Doe" name="lname">
											</div>
										</div>
										<div class="row mt-3">
											<div class="col-12 col-lg-6">
												<label for="regEmail">Email Address:</label>
												<input type="email" id="regEmail" class="form-control" placeholder="john.doe@gmail.com" name="email">
											</div>
											<div class="col-12 col-lg-6">
												<label for="regBirthDate">Date of Birth:</label>
												<input type="date" id="regBirthDate" class="form-control" name="date">
											</div>
										</div>
										<div class="row mt-3">
											<div class="col-12 col-lg-6">
												<label for="regEmail">Create Password:</label>
												<input type="password" id="pass1" class="form-control" placeholder="******" name="pass">
											</div>
											<div class="col-12 col-lg-6">
												<label for="regEmail">Confirm Password:</label>
												<input type="password" id="pass2" class="form-control" placeholder="******">
											</div>
										</div>
										<div class="row mt-3">
											<div class="col-12">
												<button type="submit" class="btn btn-dark">Register <i class="fa fa-angle-right"></i></button>
											</div>
										</div>
									</fieldset>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
	    </section>

    	`);
}





	


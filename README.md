# tacos-spring
This is a small SpringBoot RESTful API application written as part of the technical assessment for a Full Stack Developer role with PWV Consultants.

The challenge of ths assessment was to take the code located in this repo https://github.com/pwvpwv/tacos
and perform the following challenges:

> #### Challenge 1:
> Take the code in the repo and analyze it, run it, do whatever you need to so that you understand it. Pick any language on your resume (feel free to challenge us and yourself) that is not Javascript. 
> 
> #### Challenge 2:
> Now that you have translated the codebase time for an integration problem. Look for the _notTacos folder and find the passage in textSample.txt. 
> 
> ###### Criteria:
> Create an algorithm that will take a passage of text and remove all words that contain more than these letters “RSTLN AEIOU” and any one other letter. 
>  
> Note the letters “RSTLN AEIOU” and the additional letter may be used multiple times in a word.
> 
> For Example: "unclean" uses "N" twice, "LNAEU" are part of "RSTLN AEIOU" and has only one other letter "C", therefore this an acceptable word. "households" which contains "H” and “D" has more than one letter outside of our acceptable "RSTLN AEIOU" set so it must be removed. 
> 
> Note the “D” and “H” again in "households" with  "RSTLN AEIOU" - remove word , where "unclean" only uses a “C” with  "RSTLN AEIOU" - leave word
> 
> The algorithm should be able to return the following: 
> 
> An array of the remaining words ordered by length - no duplicates.
> The most common word and number of uses as an object. 
> e.g.   {word:”word”,numberOfUses:2}.
>  
> Create and integrate with newly built taco api an endpoint that will allow you to process a block of text with the above algorithm and return the algorithm’s output as JSON.
> Pass the text in the textSample.txt file through your endpoint and save the results in a file called textSampleResults.json



